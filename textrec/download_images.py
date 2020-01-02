from __future__ import absolute_import, division, print_function
import json
import os
import io
from PIL import Image
import requests
def persist_image(folder_path:str,url:str,name:str):
    image_content = None
    try:
        image_content = requests.get(url).content

    except Exception as e:
        print(f"ERROR - Could not download {url} - {e}")

    try:
        image_file = io.BytesIO(image_content)
        image = Image.open(image_file).convert('RGB')
        file_path = os.path.join(folder_path,name)
        with open(file_path, 'wb') as f:
            image.save(f, "JPEG", quality=85)
        print(f"SUCCESS - saved {url} - as {file_path}")
    except Exception as e:
        print(f"ERROR - Could not save {url} - {e}")
        

def main():
    print("hallå")
    filename = "beer_images_price.json"
    output_dir = "downloaded_images"
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    with open(filename) as json_file:
        data = json.load(json_file)
        for b in data['Beer']:
            print("Downloading to folder: " + output_dir)
            split = b['image_url'].split('/')
            newName = split[-1]
            print("new filename: " + newName)
            persist_image(output_dir, b['image_url'], newName)


if __name__ == '__main__':
    main()