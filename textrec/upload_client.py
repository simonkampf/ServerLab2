from __future__ import print_function
import requests
import json
import cv2

addr = 'http://localhost:5000'
test_url = addr + '/images/upload'

content_type = 'image/jpeg'

headers = {'content-type': content_type}

img = cv2.imread('test3.jpg')

_, img_encoded = cv2.imencode('.jpg', img)

print(img_encoded.tostring())
response = requests.post(test_url, data=img_encoded.tostring(), headers=headers)

print(json.loads(response.text))