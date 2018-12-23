# Java Ray Tracer
This is my implementation of a Ray rayTracer built from scratch in Java. Below are a list of the objects it can currently render, a list of its current features, and a list of the features that are in the works.

Here is a sample image the current version rendered:

![Sample Image](https://github.com/kahliloppenheimer/Java-Ray-Tracer/blob/master/demo_image.png?raw=true)


## Currently Supported Shapes
- Spheres
- Planes

## Implemented Features
- Anti-aliasing (with specifiable number of samples per pixel)
- Shadows (with toggle)
- Colored materials and colored lighting
- Diffuse, Specular, and Ambient lighting (all very basic and following Phong model)
- Multi-threaded rendering (with specifiable number of threads)
- Affine transformations (or any linear transformations)

## Developing Features
- Dynamic/soft shadows (sample randomly-distributed rays to determine shading)
- Dynamic ambient lighting (sample randomly-distributed rays to determine ambient light at point)
- More objects (cones, cylinders, etc.)
- Reflections/Refractions
