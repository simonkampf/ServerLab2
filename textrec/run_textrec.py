from __future__ import absolute_import, division, print_function
from classify_images import run_model

def main():
    run_model("new_images/*", "image_vectors")
    exec(open("./cluster_vectors.py").read())
    
if __name__ == '__main__':
    main()