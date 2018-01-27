# SkinDoc

## Overview

An android application created to aid in assistance of diagnosing [melanoma](https://www.skincancer.org/skin-cancer-information/melanoma), in simple words, skin cancer.
If one wants to proceed further, they can book an appointment using the app to the nearest diagnostician.
We particpated in the [Juspay HyperHack](https://www.hackathon.com/event/hyperhack---shaastra-2018-41215694251) representing [GDG-VIT](http://gdgvitvellore.com/) and presented it there.

## Details

This application takes picture of a lesion and passes it to an REST API written in Tornado, which makes prediction of the image as a benign or malignant cancer.

### Python

 * [tornado](http://www.tornadoweb.org/en/stable/) - a Python web framework and asynchronous networking library, invented to face `C10K` problem.
 * [pytorch](http://pytorch.org/) - A Deep Learning library, here used for Computer vision. We used ResNet50 architecture.
 * [pymongo](https://api.mongodb.com/python/current/) -  Python driver for the NoSQL database.
 

