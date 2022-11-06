[![License](https://img.shields.io/:license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)


jsf-renderer-extensions
=======================

Extensions on the JSF renderer to have security and advanced validation.

* Targeted at Jakarta EE 10
* Centered around CDI
* Based on concepts of MyFaces ExtVal

Compiled for Java 11

You can use version 0.9.0.3 if you still need Java EE 8 / Jakarta EE support.

Release Notes
====

1.0.0
=== 

Previous Java EE releases
=== 

This 0.9 release contains some changes which aren't backwards compatible.

1. Package names are changed, in general from **be.rubus.web.** to **be.atbash.ee.jsf**.

2. Since it is targeted as Java EE 7+, it makes use of the _CDI.select_ construct and no longer the _BeanManagerProvider_ and _BeanProvider_ classes which where copied from the _Apache DeltaSpike_ project.
So these are no longer available when used in your own application code. Alternatives are

- **CDI.select().getBeanManager()** to have access to the bean manager.
- **CDIUtils** class to retrieve individual beans or all implementations of a certain interface.

The usage of _BeanManagerFake_ within test classes is unchanged.

3. The logging of the configuration is moved from the _Jerry_ project to the _Atbash Config_ project. 

4. Jerry configuration is now using the _Atbash Config_ project reading parameters from _jerry_ base name configuration files. So no need to create a _@Specialized_ CDI bean of class _JerryConfigurator_.

If you have an application based on Jerry and/or Valerie 0.4.1, you can use the Atbash Migrator to help it migrate https://github.com/atbashEE/Atbash_Migrator