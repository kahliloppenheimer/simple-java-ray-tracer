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
- Arbitrary Polygons Meshes

## Implemented Features
- Anti-aliasing
- Shadows
- Diffuse, Specular, and Ambient lighting (Phong model)
- Reflections
- Linear transformations of objects
- Multi-threaded rendering
- Octree bounding hierarchical volume acceleration structure

## Developing Features
- Custom textures
- Dynamic/soft shadows (sample randomly-distributed rays to determine shading)
- Global illumination
- Refractions
- Reading standard polygon mesh file formats
- Optimizations (reducing # ray-triangle intersection tests)
