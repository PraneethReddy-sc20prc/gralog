#include <pybind11/pybind11.h>
#include "interface/kaHIP_interface.h"

pybind11::object wrap_kaffpa(
                const pybind11::object &vwgt,
                const pybind11::object &xadj,
                const pybind11::object &adjwgt,
                const pybind11::object &adjncy,
                int nparts,
                double imbalance,
                bool supress_output,
                int seed,
                int mode) {
        int n = pybind11::len(xadj) - 1;

        std::vector<int> xadjv, adjncyv, vwgtv, adjwgtv;
        for (auto it : xadj) 
                xadjv.push_back(pybind11::cast<int>(*it)); 

        for (auto it : adjncy) 
                adjncyv.push_back(pybind11::cast<int>(*it)); 

        for (auto it : vwgt) 
                vwgtv.push_back(pybind11::cast<int>(*it)); 

        for (auto it : adjwgt) 
                adjwgtv.push_back(pybind11::cast<int>(*it));

        int* part        = new int[n];
        int edge_cut     = 0;

        kaffpa(&n, &vwgtv[0], &xadjv[0],
               &adjwgtv[0], &adjncyv[0], &nparts,
               &imbalance, supress_output,
               seed, mode, & edge_cut, part);

        pybind11::list returnblocksArray;
        for (int i = 0; i<n; ++i)
                returnblocksArray.append(part[i]);

        delete[] part;

        return pybind11::make_tuple(edge_cut, returnblocksArray);
}

pybind11::object wrap_kaffpa_balance_NE(
                const pybind11::object &vwgt,
                const pybind11::object &xadj,
                const pybind11::object &adjwgt,
                const pybind11::object &adjncy,
                int nparts,
                double imbalance,
                bool supress_output,
                int seed,
                int mode) {
        int n = pybind11::len(xadj) - 1;

        std::vector<int> xadjv, adjncyv, vwgtv, adjwgtv;
        for (auto it : xadj)
                xadjv.push_back(pybind11::cast<int>(*it));

        for (auto it : adjncy)
                adjncyv.push_back(pybind11::cast<int>(*it));

        for (auto it : vwgt)
                vwgtv.push_back(pybind11::cast<int>(*it));

        for (auto it : adjwgt)
                adjwgtv.push_back(pybind11::cast<int>(*it));

        int* part        = new int[n];
        int edge_cut     = 0;

        kaffpa_balance_NE(&n, &vwgtv[0], &xadjv[0],
               &adjwgtv[0], &adjncyv[0], &nparts,
               &imbalance, supress_output,
               seed, mode, & edge_cut, part);

        pybind11::list returnblocksArray;
        for (int i = 0; i<n; ++i)
                returnblocksArray.append(part[i]);

        delete[] part;

        return pybind11::make_tuple(edge_cut, returnblocksArray);
}

pybind11::object wrap_node_separator(
                const pybind11::object &vwgt,
                const pybind11::object &xadj,
                const pybind11::object &adjwgt,
                const pybind11::object &adjncy,
                int nparts,
                double imbalance,
                bool supress_output,
                int seed,
                int mode) {
        int n = pybind11::len(xadj) - 1;

        std::vector<int> xadjv, adjncyv, vwgtv, adjwgtv;
        for (auto it : xadj)
                xadjv.push_back(pybind11::cast<int>(*it));

        for (auto it : adjncy)
                adjncyv.push_back(pybind11::cast<int>(*it));

        for (auto it : vwgt)
                vwgtv.push_back(pybind11::cast<int>(*it));

        for (auto it : adjwgt)
                adjwgtv.push_back(pybind11::cast<int>(*it));

        int* num_separator_vertices = new int[n];
        int** separator = (int**)calloc(*num_separator_vertices, sizeof(int));

        node_separator(&n, &vwgtv[0], &xadjv[0],
               &adjwgtv[0], &adjncyv[0], &nparts,
               &imbalance, supress_output,
               seed, mode, num_separator_vertices, separator);

        pybind11::list returnblocksArray;

        for (int i = 0 ; i< *num_separator_vertices; ++i)
        {
            returnblocksArray.append((*separator)[i]);
        }


        delete[] separator;

        return pybind11::make_tuple(num_separator_vertices, returnblocksArray);
}

pybind11::object wrap_reduced_nd(
                const pybind11::object &xadj,
                const pybind11::object &adjncy,
                bool supress_output,
                int seed,
                int mode) {
        int n = pybind11::len(xadj) - 1;
        std::vector<int> xadjv, adjncyv;
        for (auto it : xadj)
                xadjv.push_back(pybind11::cast<int>(*it));

        for (auto it : adjncy)
                adjncyv.push_back(pybind11::cast<int>(*it));

        int* ordering        = new int[n];

        reduced_nd(&n, &xadjv[0],
               &adjncyv[0],
               supress_output,
               seed, mode, ordering);

        pybind11::list returnblocksArray;
        for (int i = 0; i<n; ++i)
                returnblocksArray.append(ordering[i]);

        return pybind11::make_tuple(0, returnblocksArray);
}

PYBIND11_MODULE(kahip, m) {
        m.doc() = "pybind11 example plugin"; // optional module docstring
        m.def("kaffpa", &wrap_kaffpa, "A function that partitions a graph.");
        m.def("kaffpa_balance_NE", &wrap_kaffpa_balance_NE, "A program to balance nodes and edges among the blocks.");
        m.def("node_separator", &wrap_node_separator, "A node separator program.");
        m.def("reduced_nd", &wrap_reduced_nd, "A node ordering program.");
}


