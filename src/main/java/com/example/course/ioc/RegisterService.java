package com.example.course.ioc;

/**
 *  1. add interface
 *  2. all components need to impl interface
 *  3. @Autowired -> inject by type
 *                   if we have multiple implementations of current type => throw exception
 *  4. @Autowired + @Qualifier("name") -> inject by bean name
 *  5. provide constructor injection
 *      @Autowired
 *      public xx(.. ,..) {
 *          .. = ..
 *          .. = ..
 *      }
 *  6. provide setter injection
 *  7. provide different injection scope / bean scope
 *          1. now we only supporting singleton
 *          2. prototype -> @Autowired => you inject a new instance
 */

/**
 * The interface for student register service
 */
public interface RegisterService {

}
