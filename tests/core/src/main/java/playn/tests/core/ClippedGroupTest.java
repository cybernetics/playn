/**
 * Copyright 2012 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.tests.core;

import pythagoras.f.FloatMath;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import static playn.core.PlayN.graphics;
import playn.core.ImmediateLayer;
import playn.core.Surface;

public class ClippedGroupTest extends Test {

  private float elapsed, rotation;
  private GroupLayer.Clipped g1, g2;
  private ImageLayer i1;

  @Override
  public String getName() {
    return "ClippedGroupTest";
  }

  @Override
  public String getDescription() {
    return "Tests clipping of children in group layers.";
  }

  @Override
  public void init() {
    GroupLayer rootLayer = graphics().rootLayer();

    final CanvasImage img = graphics().createImage(100, 50);
    img.canvas().setFillGradient(graphics().createLinearGradient(
                                   0, 0, 100, 100, new int[] { 0xFF0000FF, 0xFF00FF00 },
                                   new float[] { 0, 1 }));
    img.canvas().fillRoundRect(0, 0, 100, 50, 10);

    // create an immediate layer that draws the boundaries of our clipped group layers
    rootLayer.add(graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
      public void render(Surface surf) {
        // draw the border of our various clipped groups
        surf.setFillColor(0xFF000000);
        drawRect(surf, g1.transform().tx(), g1.transform().ty(), g1.width(), g1.height());
        drawRect(surf, g2.transform().tx(), g2.transform().ty(), g2.width(), g2.height());
      }
      protected void drawRect(Surface surf, float x, float y, float w, float h) {
        float left = x-1, top = y-1, right = x+w+2, bot = y+h+2;
        surf.drawLine(left, top, right, top, 1);
        surf.drawLine(right, top, right, bot, 1);
        surf.drawLine(left, top, left, bot, 1);
        surf.drawLine(left, bot, right, bot, 1);
      }
    }));

    // create a group layer with a static clip, and a rotating image inside
    g1 = graphics().createGroupLayer(100, 100);
    i1 = graphics().createImageLayer(img);
    i1.setOrigin(i1.width()/2, i1.height()/2);
    g1.addAt(i1, 50, 50);
    rootLayer.addAt(g1, 25, 25);

    g2 = graphics().createGroupLayer(100, 100);
    g2.addAt(graphics().createImageLayer(img), (100 - img.width())/2, (100 - img.height())/2);
    rootLayer.addAt(g2, 150, 25);
  }

  @Override
  public void update(float delta) {
    elapsed += delta/1000;
    i1.setRotation(elapsed * FloatMath.PI/2);
    g2.setWidth(Math.max(1f, Math.abs(100 * FloatMath.sin(elapsed))));
  }
}