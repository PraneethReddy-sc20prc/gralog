#!/usr/bin/env python

from Gralog import *

g = Graph(None)

lines = []

with open('rgg_n_2_15_s0.graph') as file:
    for line in file:
        lines.append(line)



#!/usr/bin/env python
# import sys
#
# sys.path.insert(1, '/Users/praneethreddychintakindi/Desktop/Leeds-AdvanceComputerScience/MscProject/gralog-master/KaHIP-master/deploy')
# import kahip;
#
# from Gralog import *
#
# g = Graph(None)
#
# adjncyList = []
# counter = 0
# allverticesIds = []
# adjncy = []
# xadj = [0]
# vwgt = []
# adjcwgt = []
# supress_output = 0
# imbalance = 0.0
# nblocks = 3
# seed = 0
# mode = 2
#
# allVertices = g.getAllVertices()
# noOfVertices = len(allVertices)
# for eachVertex in allVertices:
#     vwgt.append(1)
#     allverticesIds.append(eachVertex.getId())
#     neighboursList = []
#     for eachNeighbour in eachVertex.getNeighbours():
#         adjcwgt.append(1)
#         neighboursList.append(eachNeighbour.getId())
#     xadj.append(counter + len(neighboursList))
#     counter = counter + len(neighboursList)
#     neighboursList.sort()
#     adjncyList.append(neighboursList)
#
# adjncy = [item for sublist in adjncyList for item in sublist]
#
# edgecut, parts = kahip.kaffpa(vwgt, xadj, adjcwgt, adjncy, nblocks, imbalance, supress_output, seed, mode)
# # gPrint(xadj)
# # gPrint(adjncy)
# # gPrint(edgecut)
# # gPrint(parts)
# #
# # for eachVertex in parts:
# #     g.deleteVertex(eachVertex)
#
#
# allParts = []
# for i in list(set(parts)):
#     eachPart = []
#     for j in range(len(allverticesIds)):
#         if i == parts[j]:
#             eachPart.append(allverticesIds[j])
#     allParts.append(eachPart)
#
#
# allPartEdges = []
# for part in allParts:
#     for i in range(len(part)):
#         for j in range(len(part)):
#             if i - j > 0:
#                 temp = (part[i], part[j])
#                 if g.existsEdge((part[i], part[j])):
#                     allPartEdges.append([part[i], part[j]])
# gPrint(allPartEdges)
#
#
# for eachEdge in g.getAllEdges():
#     g.deleteEdge(eachEdge)
#
#
# for eachEdge in allPartEdges:
#     g.addEdge(eachEdge[0], eachEdge[1])
#
#
#
# for eachVertex in allVertices:
#     c = eachVertex.getProperty("coordinates")[9:-1].split(",")
#     xc = float(c[0])
#     yc = float(c[1])
#     for i in range(len(allParts)):
#         if eachVertex.getId() in allParts[i]:
#             eachVertex.setCoordinates([(i*(noOfVertices+10))+xc,None])
#
# gPrint('Number of edge cuts ->' + str(edgecut))






