package com.example.akash.myhighway1.user;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * This is the <h3>UserScope</h3>
 * created by Vishal Anaghan
 * <ul>
 *    <li>
 *        It can be valid only in same package
 *    </li>
 * </ul>
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {

}
