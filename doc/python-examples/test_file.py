#!/usr/bin/env python

from graph_partitioning.Gralog import *

g = Graph(None)

adjncy = []
adjncyList = []
xadj = [0]
counter = 0

allVertices = g.getAllVertices()
for eachVertex in allVertices:
    neighbours = eachVertex.getNeighbours()
    neighboursList = []
    for eachNeighbour in eachVertex.getNeighbours():
        neighboursList.append(eachNeighbour.getId())
    xadj.append(counter + len(neighboursList))
    counter = counter + len(neighboursList)
    neighboursList.sort()
    adjncyList.append(neighboursList)

adjncy = [item for sublist in adjncyList for item in sublist]

gPrint('')
gPrint(adjncy)
gPrint(xadj)
gPrint('')
