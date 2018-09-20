package com.salakheev.shaderbuilderkt.annotations

/**
 * Annotation is used to limit the generation range of Int type parameters.
 * [ShaderIntRange] must be provided with all [Int] type parameters.
 *
 * As a result all values in [from]..[to] range will be used as parameter values
 * @Example:
 *  class ShaderProgram(param0: Boolean, @ShaderIntRange(to = 2)param1: Int)
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class ShaderIntRange(val from: Int = 1, val to: Int = 1)
