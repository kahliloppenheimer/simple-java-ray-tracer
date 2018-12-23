package me.kahlil.graphics;

import me.kahlil.scene.Ray3D;

interface AntiAliaser {

  Color antiAlias(Ray3D ray);

}
