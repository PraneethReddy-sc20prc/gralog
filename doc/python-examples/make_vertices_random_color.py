#!/usr/bin/env python3

from random import seed, random
from graph_partitioning.Gralog import *

###### MAIN #####

g = Graph(None)
vertices = g.getVertices()
seed(3)
for v in vertices:
    color = int((random()*100000000))% 16777215
    v.setColor('#' + str(color))

