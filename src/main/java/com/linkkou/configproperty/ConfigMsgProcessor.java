package com.linkkou.configproperty;


import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Pair;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.*;

/**
 * ATP 生成方法
 *
 * @author LK
 * @date 2018-05-31 10:46
 */
@SupportedAnnotationTypes({"com.linkkou.configproperty.ConfigValue"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ConfigMsgProcessor extends AbstractProcessor {

    private Trees trees;
    private TreeMaker make;
    private Name.Table names;
    private Context context;

    /**
     * 初始化
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        trees = Trees.instance(processingEnv);
        context = ((JavacProcessingEnvironment)
                processingEnv).getContext();
        make = TreeMaker.instance(context);
        names = Names.instance(context).table;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 依据相关注解解析
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ConfigValue.class.getCanonicalName());
    }

    /**
     * {@inheritDoc}
     *
     * @param annotations
     * @param roundEnv
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //从注解中找到所有的类,以及对应的注解的方法
        List<TypeElement> targetClassMap = findAnnoationElement(roundEnv);
        //生成每一个类和方法
        for (TypeElement item : targetClassMap) {
            JCTree tree = (JCTree) trees.getTree(item);
            TreeTranslator visitor = new Inliner();
            tree.accept(visitor);
        }
        return true;
    }


    /**
     * 查询所有带有{@link ConfigValue ConfigValue注解的}
     *
     * @param roundEnvironment
     * @return
     */
    private List<TypeElement> findAnnoationElement(RoundEnvironment roundEnvironment) {
        List<TypeElement> targetClassMap = new ArrayList<>();
        //找到所有跟AnDataCollect注解相关元素
        Collection<? extends Element> anLogSet = roundEnvironment.getElementsAnnotatedWith(ConfigValue.class);
        //遍历所有元素
        for (Element e : anLogSet) {
            //注解用于字段
            if (e.getKind() != ElementKind.FIELD) {
                continue;
            }
            //此处找到的是类的描述类型，因为我们的AnDataCollect的注解描述是method的所以closingElement元素是类
            TypeElement enclosingElement = (TypeElement) e.getEnclosingElement();
            //对类做一个缓存
            targetClassMap.add(enclosingElement);
        }
        return targetClassMap;
    }

    /**
     * 代码树结构修改
     */
    private class Inliner extends TreeTranslator {

        /**
         * 变量的处理
         *
         * @param var1
         */
        @Override
        public void visitVarDef(JCTree.JCVariableDecl var1) {
            super.visitVarDef(var1);

            if (var1.mods.getAnnotations() == null || var1.mods.getAnnotations().size() == 0) {
                return;
            }

            //<editor-fold desc="获取到代码注解上的值">
            String configvalue = "";
            Object defaultvalue = null;
            boolean isConfigValue = false;
            for (JCTree.JCAnnotation jcAnnotation : var1.mods.getAnnotations()) {
                if (jcAnnotation.getAnnotationType().type.toString().equals("com.linkkou.configproperty.ConfigValue")) {
                    for (Pair<Symbol.MethodSymbol, Attribute> pair : jcAnnotation.attribute.values) {
                        //Value
                        if (pair.fst.toString().equals("value()")) {
                            if (pair.snd.getValue() instanceof Attribute.Compound) {
                                final Attribute.Compound value = (Attribute.Compound) pair.snd.getValue();
                                if (value.type.toString().equals("org.springframework.beans.factory.annotation.Value")) {
                                    Attribute.Compound ac = (Attribute.Compound) pair.snd.getValue();
                                    for (Pair<Symbol.MethodSymbol, Attribute> pair2 : ac.values) {
                                        configvalue = (String) pair2.snd.getValue();
                                        isConfigValue = true;
                                    }
                                }
                            }
                        }
                        //defaultValue
                        if (pair.fst.toString().equals("defaultValue()")) {
                            if (pair.snd.getValue() instanceof String) {
                                defaultvalue = (String) pair.snd.getValue();
                            }
                        }
                    }
                }
            }
            if (!isConfigValue) {
                return;
            }
            //</editor-fold>

            //<editor-fold desc="匹配ConfigUtils类中相关的方法">
            boolean isConfig = false;
            JCTree.JCExpression vartype = var1.vartype;
            String val = "";
            if (vartype instanceof JCTree.JCTypeApply) {
                if ("com.linkkou.configproperty.Config".equals(vartype.type.tsym.toString())) {
                    com.sun.tools.javac.util.List<JCTree.JCExpression> arguments = ((JCTree.JCTypeApply) var1.vartype).arguments;
                    if (arguments.length() == 1) {
                        val = arguments.get(0).toString();
                        isConfig = true;
                    }
                } else {
                    return;
                }
            } else if (vartype instanceof JCTree.JCIdent) {
                val = ((JCTree.JCIdent) vartype).name.toString();
                if (var1.init != null) {
                    if (var1.init instanceof JCTree.JCLiteral) {
                        final JCTree.JCLiteral init = (JCTree.JCLiteral) var1.init;
                        defaultvalue = init.value;
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }

            //变量的调用new方法  new ConfigUtils("*");
            JCTree.JCExpression loggerNewClass = make.NewClass(null,
                    null,
                    //类名称 会自己导入包，但是要完整的路径
                    make.Select(
                            make.Select(
                                    make.Select(make.Ident(names.fromString("com")), names.fromString("linkkou"))
                                    , names.fromString("configproperty")
                            ), names.fromString("ConfigUtils")),
                    //参数
                    com.sun.tools.javac.util.List.of(
                            make.Literal(configvalue),
                            defaultvalue != null ? make.Literal(defaultvalue) : make.Literal(TypeTag.BOT, null)
                    ),
                    null);
            /*Class classs = ConfigUtils.class;
            for (Method m : classs.getMethods()) {
                if (m.getName().equals("get" + jcIdent.name.toString())) {
                    name = names.fromString(m.getName());
                }
            }*/
            //new ConfigUtils("*").*;
            JCTree.JCExpression loggerType2 = make.Select(
                    loggerNewClass,
                    names.fromString("getConfig"));

            //new ConfigUtils("*").*()
            JCTree.JCMethodInvocation getLoggerCall = make.Apply(
                    com.sun.tools.javac.util.List.nil(),
                    //构建 -> getProxy()
                    loggerType2,
                    //参数
                    com.sun.tools.javac.util.List.of(
                            make.Literal(val),
                            make.Literal(isConfig)
                    ));
            //</editor-fold>

            // private string name = new ConfigUtils("*").*()
            JCTree.JCVariableDecl jcv = make.VarDef(
                    var1.getModifiers(),
                    var1.name,
                    var1.vartype,
                    //构建 -> (TestInterface) ***
                    getLoggerCall
            );
            this.result = jcv;
        }
    }
}