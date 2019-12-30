# Java Ray Tracer
This is my implementation of a Ray rayTracer built from scratch in Java. Below are a list of the objects it can currently render, a list of its current features, and a list of the features that are in the works.

Here is a sample image the current version rendered:

![Sample Image](https://github.com/kahliloppenheimer/Java-Ray-Tracer/blob/master/images/polygons/demo-10.png?raw=true)

## How to run
Run:

```
mvn clean package

```
Then execute `demo.java` and take a look at images/tmp.

## Currently Supported Shapes
- Spheres
- Planes
- Triangles
- Polygons

## Implemented Features
- Anti-aliasing (with specifiable number of samples per pixel)
- Shadows (with toggle)
- Colored materials and colored lighting
- Diffuse, Specular, and Ambient lighting (all very basic and following Phong model)
- Reflections
- Multi-threaded rendering (with specifiable number of threads)
- Affine transformations (or any linear transformations)

## Developing Features
- Dynamic/soft shadows (sample randomly-distributed rays to determine shading)
- Dynamic ambient lighting (sample randomly-distributed rays to determine ambient light at point)
- More objects (cones, cylinders, etc.)
- Refractions
- Reading standard polygon mesh file formats
