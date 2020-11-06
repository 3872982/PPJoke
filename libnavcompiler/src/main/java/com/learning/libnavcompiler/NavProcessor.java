package com.learning.libnavcompiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.learning.libnavannotation.ActivityDestination;
import com.learning.libnavannotation.FragmentDestination;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * APP页面导航注解处理器
 * ===========================================================================================
 * AutoService 这个标记申明，annotationProcessor  project()应用一下,编译就能自动执行该类了。
 * ===========================================================================================
 * SupportedSourceVersion 声明支持的JDK版本
 * ==================================================
 * SupportedAnnotationTypes:声明该处理器要处理那些注解
 * ======================================================
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.learning.libnavannotation.ActivityDestination","com.learning.libnavannotation.FragmentDestination"})
public class NavProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;
    private static final String OUTPUT_FILE_NAME = "destination.json";
    private HashMap<String, JSONObject> mDestMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //文件处理器
        mFiler = processingEnvironment.getFiler();
        //日志打印，在java环境下不能使用android.util.log.e打印日志
        mMessager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //通过处理器环境上下文roundEnv分别获取 项目中标记的FragmentDestination.class 和ActivityDestination.class注解。
        //此目的就是为了收集项目中哪些类 被注解标记了
        Set<? extends Element> activityElements = roundEnvironment.getElementsAnnotatedWith(ActivityDestination.class);
        Set<? extends Element> fragmentElements = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);

        //处理注解标记过的类，获取他们中的注解内容并存储到HashMap中
        if(!activityElements.isEmpty() || !fragmentElements.isEmpty()){
            mDestMap = new HashMap<>();

            handleDestination(activityElements,ActivityDestination.class, mDestMap);
            handleDestination(fragmentElements,FragmentDestination.class, mDestMap);
        }

        //写入文件中
        //app/src/main/assets
        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            //filer.createResource()意思是创建源文件
            //我们可以指定为class文件输出的地方，
            //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
            //StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
            //StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了
            FileObject resource = mFiler.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
            String resourcePath = resource.toUri().getPath();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "resourcePath:" + resourcePath);

            //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
            //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径
            String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
            String assetsPath = appPath + "src/main/assets/";

            File file = new File(assetsPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            //此处就是稳健的写入了
            File outPutFile = new File(file, OUTPUT_FILE_NAME);
            if (outPutFile.exists()) {
                outPutFile.delete();
            }
            outPutFile.createNewFile();

            //利用fastjson把收集到的所有的页面信息 转换成JSON格式的。并输出到文件中
            String content = JSON.toJSONString(mDestMap);
            fos = new FileOutputStream(outPutFile);
            writer = new OutputStreamWriter(fos, "UTF-8");
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    //或许标记过的注解类的注解内容，并存放到destMap中
    private void handleDestination(Set<? extends Element> elements, Class<? extends Annotation> annotationClz, HashMap<String, JSONObject> destMap) {

        for (Element element : elements) {
            //TypeElement是Element中的一种
            //如果我们的注解是注解在类上的，那么可以使用TypeElement获取类的全类名
            TypeElement typeElement = (TypeElement) element;
            //com.learning.MyPPJoke.HomeFrament
            String clzName = typeElement.getQualifiedName().toString();
            //页面节点的id，ID不能重复，这里使用全类名的hashcode作为ID
            int id = Math.abs(clzName.hashCode());
            //页面的pageUrl 相当于 页面隐式跳转的URI [scheme:][//host:port][path][?query][#fragment]
            String pageUrl = null;
            //页面是否需要登陆
            boolean needLogin = false;
            //是否作为起始页
            boolean asStart = false;
            //true为fragment，false为activity
            boolean isFragment = false;

            Annotation annotation = element.getAnnotation(annotationClz);
            if(annotation instanceof FragmentDestination){
                FragmentDestination destination = (FragmentDestination) annotation;
                pageUrl = destination.pageUrl();
                needLogin = destination.needLogin();
                asStart = destination.asStart();
                isFragment = true;
            }else if(annotation instanceof  ActivityDestination){
                ActivityDestination destination = (ActivityDestination) annotation;
                pageUrl = destination.pageUrl();
                needLogin = destination.needLogin();
                asStart = destination.asStart();
                isFragment = false;
            }

            if(destMap.containsKey(pageUrl)){
                mMessager.printMessage(Diagnostic.Kind.NOTE,"页面上的PageURL："+pageUrl+"必须唯一，请修改ClassName："+clzName);
            }else{
                JSONObject object = new JSONObject();
                object.put("id",id);
                object.put("className",clzName);
                object.put("pageUrl",pageUrl);
                object.put("needLogin",needLogin);
                object.put("asStart",asStart);
                object.put("isFragment",isFragment);
                //添加到HashMap中
                destMap.put(pageUrl,object);
            }
        }
    }
}
