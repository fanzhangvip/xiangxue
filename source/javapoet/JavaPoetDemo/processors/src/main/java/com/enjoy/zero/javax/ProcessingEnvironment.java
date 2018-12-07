package com.enjoy.zero.javax;

import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.SourceVersion;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public interface ProcessingEnvironment {

    /**
     * 返回用来在元素上进行操作的某些实用工具方法的实现。<br>
     *
     * Elements是一个工具类，可以处理相关Element（包括ExecutableElement, PackageElement, TypeElement, TypeParameterElement, VariableElement）
     */
    Elements getElementUtils();

    /**
     * 返回用来报告错误、警报和其他通知的 Messager。
     */
    Messager getMessager();

    /**
     *  用来创建新源、类或辅助文件的 Filer。
     */
    Filer getFiler();

    /**
     *  返回用来在类型上进行操作的某些实用工具方法的实现。
     */
    Types getTypeUtils();

    // 返回任何生成的源和类文件应该符合的源版本。
    SourceVersion getSourceVersion();

    // 返回当前语言环境；如果没有有效的语言环境，则返回 null。
    Locale getLocale();

    // 返回传递给注释处理工具的特定于 processor 的选项
    Map<String, String> getOptions();
}