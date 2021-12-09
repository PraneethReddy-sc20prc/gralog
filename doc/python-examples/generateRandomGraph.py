#!/usr/bin/env python

from graph_partitioning.Gralog import *


g = Graph()

g.generateRandomGraph(50,0.09)

gPrint(str(len(g.getVertices())))
