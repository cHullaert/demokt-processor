package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.javaToKotlinType
import com.squareup.kotlinpoet.*
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class FunctionGenerator(clazz: TypeElement, fields: Iterable<VariableElement>): AbstractGenerator(clazz, fields) {

    override fun build(): FileSpec {
        return FileSpec.builder(getPackageName(), FUNCTION_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addFunction(buildFunction())
            .build()
    }

    fun buildFunction(): FunSpec {
        val lambda= LambdaTypeName.get(receiver = TypeVariableName(BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString())),
            returnType = Unit::class.asClassName())

        return FunSpec.builder(clazz.simpleName.toString().decapitalize())
            .addParameter("block", lambda)
            .returns(clazz.javaToKotlinType())
            .addStatement("return %s().apply(block).build()".format(BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString())))
            .build()
    }

}