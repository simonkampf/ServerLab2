from flask import Flask, request, Response
import numpy as np
import cv2
import jsonpickle
import os
import uuid
import json
import base64
from move_used_images import moveImages
from classify_images import run_classify
from cluster_vectors import cluster

app = Flask(__name__)


@app.route("/")
def hello():
    return "hello world"

# use upload_test.py to test this...
@app.route('/images/upload', methods=['POST'])
def post_image():
    r = request

    img = base64.b64decode(r.data)

    filename = str(uuid.uuid4())
    print(filename)

    with open("new_images/" + filename + ".jpg", 'wb') as f:
        f.write(img)
    #cv2.imwrite('new_images/' + filename + ".jpg", img)

    run_classify()

    moveImages()

    cluster()

    folderpath = "nearest_neighbors/"
    allImages = os.listdir(folderpath)
    imageName = filename + ".json"
    imageName_local = ""
    for i in allImages:
        if(i == imageName):
            imageName_local = i
    if(imageName_local == ""):
        return Response(jsonpickle.encode({'imagename': "no such image"}), status=404)
    res = getNeighbors(imageName_local)

    return jsonpickle.encode(res)


@app.route('/images/getSimilarImages/<imageName>', methods=['GET'])
def getSimilarImages(imageName):
    folderpath = "nearest_neighbors/"
    allImages = os.listdir(folderpath)
    # print(allImages)
    imageName = imageName + ".json"
    imageName_local = ""
    for i in allImages:
     #   print(i)
        if(i == imageName):
            imageName_local = i
    if(imageName_local == ""):
        return Response(jsonpickle.encode({'imagename': "no such image"}), status=404)
    res = getNeighbors(imageName_local)

    return jsonpickle.encode(res)


def getNeighbors(imagename):

    folderPath = 'nearest_neighbors/'
    with open(folderPath + imagename) as json_file:
        res = []
        closestNeighbors = json.load(json_file)

        for f in closestNeighbors:
            res.append(
                {'filename': str(f['filename']), 'similarity': str(f['similarity'])})
        return res


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
