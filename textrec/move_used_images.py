from __future__ import absolute_import, print_function
import os
import shutil  

def moveImages():
    dest = "image_storage/"
    src = "new_images/"
    images = os.listdir(src)
    for i in images:
        shutil.move(src + i, dest)
    