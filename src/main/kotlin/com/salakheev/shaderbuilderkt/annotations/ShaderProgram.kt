package com.salakheev.shaderbuilderkt.annotations

import com.salakheev.shaderbuilderkt.sources.ShaderSourceProvider
import kotlin.reflect.KClass

/**
 * Annotation is used to automatically generate shader program sources
 * with all possible parameter values during gradle kaptKotlin task.
 * Class annotated with [ShaderProgram] must have one constructor
 * with all parameters provided in [vertex] and [fragment] constructors.
 * Only [Boolean] and [Int] (with [ShaderIntRange] annotation) are currently supported
 * as constructor parameter types.
 *
 * Example:
 *  For a com.example.Program class annotated with ShaderProgram
 *  a com.example.ProgramSources class will be created
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ShaderProgram(val vertex: KClass<out ShaderSourceProvider>, val fragment: KClass<out ShaderSourceProvider>)
