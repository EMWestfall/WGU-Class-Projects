# This class takes as input a distance matrix (represented by the set of locations.distance_list for all
# locations). From there it does a k-medoids algorithm on that matrix to cluster each location.

class KMedoids:
    from Location import Location

    def __init__(self, k=2, locs=Location.locations, maximum_iterations=1000):
        from math import ceil
        from math import floor
        self.k = k
        self.locations = locs
        location_nums = [loc.number for loc in self.locations.return_entire_table()]
        location_set = set(location_nums[0:floor(k/2)]).union(set(location_nums[len(location_nums) - ceil(k/2)::]))

        self.medoids = [self.locations.search(i) for i in location_set]
        self.medoids_cost = [0 for _ in range(k)]
        self.cluster_map = []
        self.clusters = [[] for _ in range(k)]
        for _ in range(maximum_iterations):
            self.associate()
            # If true, set is converged
            if self.update_medoids():
                break
        self.associate()

    def __str__(self):
        output = ""
        for i in range(self.k):
            output += f"\nCluster {i}: "
            for location in self.clusters[i]:
                output += str(location.number) + ", "
        return output

    def __iter__(self):
        return self.clusters

    def associate(self):
        from statistics import pvariance
        from statistics import mean

        self.cluster_map = []
        self.clusters = [[] for _ in range(self.k)]
        distances_for_variance = [[] for _ in range(self.k)]
        for loc in self.locations.return_entire_table():
            distances = [loc.get_distance_to(self.medoids[med_num].number) for med_num in range(self.k)]
            distances_for_variance[distances.index(min(distances))].append(min(distances))
            self.cluster_map.append(distances.index(min(distances)))
        for medoid_num in range(self.k):
            self.medoids_cost[medoid_num] = pvariance(distances_for_variance[medoid_num],
                                                      mean(distances_for_variance[medoid_num]))
        for loc_num in range(len(self.cluster_map)):
            self.clusters[self.cluster_map[loc_num]].append(self.locations.search(loc_num))

    def update_medoids(self):
        from statistics import pvariance
        from statistics import mean
        new_medoids = []
        for medoid_num in range(self.k):
            new_medoid = self.medoids[medoid_num]
            old_cost = self.medoids_cost[medoid_num]
            for location in self.clusters[medoid_num]:
                current_distances = []
                for location2 in self.clusters[medoid_num]:
                    current_distances.append(location.get_distance_to(location2.number))
                current_cost = pvariance(current_distances, mean(current_distances))
                if current_cost < old_cost:
                    new_medoid = location
                    old_cost = current_cost
            new_medoids.append(new_medoid)
        if set([medoid for medoid in self.medoids]) == set([medoid for medoid in new_medoids]):
            return True
        self.medoids = new_medoids
        return False

    # Returns a matrix of medoid distances from each other
    # Each row maps to the medoid[i] number.
    def get_medoid_matrix(self):
        from Location import Location
        matrix = []
        for medoid_from in self.medoids:
            current_row = []
            for medoid_to in self.medoids:
                current_row.append(medoid_from.distance_list[medoid_to.number][1])
            matrix.append(current_row)
        return matrix
