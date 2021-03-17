# Name: Eric Westfall, Student_ID: 000842244

from Graphs import PathGraph
from Packages import Package
from Packages import LocationType
from Packages import DeliveryStatus
from Location import Location
from Truck import Truck
from KMedoids import KMedoids
from EricTime import EricTime
from time import time as clocktime
import pandas as pd

start = clocktime()

# ---------Values that make the most difference in this program------------
# Max values to lookahead (computational complexity is very sensitive to increases - max recommended is 9, which takes
# about 150 seconds to run on my machine.
MAX_LOOKAHEAD = 9
# --------------------Don't change these values-------------------
# Absolute package limit for each truck
ABSOLUTE_PACKAGE_LIMIT = 16
# If less than 16, designates truck #2 as a shorter loop truck so it returns to the hub more often to get priority
# packages
SHORTER_PACKAGE_LIMIT = 16
# ------------------------------------------------------------------

# -------Initializations-------
# trucks
Truck(1)
Truck(2)
all_trucks = Truck.get_all_trucks()

# -------Imports-------

# ---Locations---
graph_input = pd.read_excel("WGUPS Distance Table.xlsx", header=0, index_col=0, engine="openpyxl")
locations = graph_input.columns.tolist()
# This is needed for iterating through the new locations later
try:
    start_location_num = Location.max_location_number()
except ValueError:
    start_location_num = 0
# Get the graph and distances between points
# Unfortunately I wrote the PathGraph and Dijkstra's with lists before I decided to do input via pandas
graph_input = graph_input.values.tolist()
distances = PathGraph(graph_input)
# Add locations
for num in range(len(locations)):
    location_info = locations[num].split(" - ")
    # print(distances.adjacency_matrix[num])
    Location(start_location_num + num, location_info[0], location_info[1], location_info[2], location_info[3],
             distances.adjacency_matrix[num])

# Determine Location Clusters
clusters = KMedoids(len(Truck.trucks.return_entire_table()) + 2)
# Assign a cluster to each location
for i in range(len(Location.locations.return_entire_table())):
    Location.locations.search(i).cluster = clusters.cluster_map[i]

# ---Packages---
# Import packages from the Excel file
packages = pd.read_excel("WGUPS Packages.xlsx", header=0, engine="openpyxl")
# Convert packages to a list
packages = packages.values.tolist()
# Create package objects and insert them in the Package.packages hash table
next_pack_num = 0
if Package.packages.return_entire_table():
    next_pack_num = max([pack.number for pack in Package.packages.return_entire_table()]) + 1
# Hub is used multiple times
hub = Location.locations.search(0)
for pack_num in range(len(packages) + next_pack_num):
    # Weight
    weight = packages[pack_num][5]
    # Deadline
    deadline = packages[pack_num][4]
    # Convert times to floats; makes it easier to do math
    deadline = deadline.hour + deadline.minute/60.0
    # Get delivery location
    delivery_loc = None
    for location in Location.locations.return_entire_table():
        if packages[pack_num][0] == location.address:
            delivery_loc = location.number
    # Hub Arrival Time
    hub_arrival = packages[pack_num][6]
    # Convert times to floats; makes it easier to do math
    hub_arrival = hub_arrival.hour + hub_arrival.minute/60.0
    # The truck this package has to be delivered on
    truck_requirement = packages[pack_num][7]
    # The bundle of packages this package is part of
    bundle = None
    if packages[pack_num][8] > 0:
        bundle = packages[pack_num][8]
    # 8am is the start, and if the package has an arrival of 8am or less, it's at the hub before the day starts
    # Otherwise, it is en-route to the hub
    current_location = (0, LocationType.LOCATION)
    delivery_status = DeliveryStatus.AT_HUB
    # The +1 offset is because the package id numbers in the import file indexes at 1
    new_package = Package(pack_num + 1, weight, deadline, delivery_loc,
                          hub_arrival, current_location, delivery_status,
                          truck_requirement, bundle)
    hub.packages[pack_num + 1] = new_package
    Location.locations.search(new_package.location_to_deliver_to).packages_going_here.append(new_package.number)
# ------End Imports------

# ------Run Main Loop--------
count = 0
while hub.packages and count < 10:
    # Get the truck with the lowest time
    lowest_time = 24
    truck_to_calculate = None
    for truck in all_trucks:
        if truck.time < lowest_time:
            lowest_time = truck.time
    for truck in all_trucks:
        if truck.time == lowest_time:
            truck_to_calculate = truck
            break
    # Calculate for the truck with the lowest time
    print()
    # Truck 2 has shorter routes so it can deliver priority packages
    if int(truck_to_calculate.number) == 2:
        truck_to_calculate.load_truck(min(SHORTER_PACKAGE_LIMIT, ABSOLUTE_PACKAGE_LIMIT), clusters)
    else:
        truck_to_calculate.load_truck(ABSOLUTE_PACKAGE_LIMIT, clusters)
    print()
    if truck_to_calculate.packages:
        truck_to_calculate.calculate_route(MAX_LOOKAHEAD)
    count += 1

delivered = []
for package in Package.get_all_packages():
    if package.delivery_status is DeliveryStatus.DELIVERED:
        delivered.append(package.number)

truck_paths = ""
for truck in all_trucks:
    truck_paths += f"Truck {truck.number} Path: {truck.destination_full_queue}\n"

end_string = f"\nEnd Time: {EricTime.convert_time(max([t.time for t in all_trucks]))}\n" \
      f"Packages at Hub: {[package for package in hub.packages]}\n" \
      f"Packages En Route: {[[package for package in truck.packages] for truck in all_trucks]}\n" \
      f"Packages Delivered: {delivered}\n" \
      f"{truck_paths}" \
      f"Total Distance: {sum([t.total_path_distance for t in all_trucks])} miles."

print()

# Reports
# Required Reports
time_list = [9, 10, 12.5]
for i in time_list:
    en_route_to_hub = []
    at_hub = []
    truck_map = [[] for truck in Truck.trucks.return_entire_table()]
    en_route = ""
    delivered = []
    time = i
    for package in Package.packages.return_entire_table():
        if package.hub_arrival > time:
            en_route_to_hub.append(package.number)
        elif package.loading_time > time:
            at_hub.append(package.number)
        elif package.delivery_time > time:
            truck_map[package.delivery_truck - 1].append(package.number)
        else:
            delivered.append(package.number)
    for truck in Truck.trucks.return_entire_table():
        en_route += f"\nTruck #{truck.number}: {sorted(truck_map[truck.number - 1])}"
    print(f"Package status at {EricTime.convert_time(i)}:\n"
          f"En Route to Hub: {sorted(en_route_to_hub)}\n"
          f"At Hub: {sorted(at_hub)}\n"
          f"En Route: {en_route}\n"
          f"Delivered: {sorted(delivered)}\n\n")

# Final Results
if hub.packages:
    exit(end_string + "\nPackages went undelivered because no path would meet their deadline. Try the program again.")
elif sum([t.total_path_distance for t in all_trucks]) > 140:
    exit(f"The path was too long to meet the requirements: "
         f"{sum([t.total_path_distance for t in all_trucks])}. Try the program again.")
else:
    print(end_string)

print()

print(f"Program time: {clocktime() - start} seconds.\n\n")

# Inquiries
user_input = input("Input t for truck info. Input p for package info. Input n to exit.\n")
while user_input != "n":
    if user_input == "p":
        package_num = 0
        while 1 > int(package_num) \
                or int(package_num) > max([package.number for package in Package.packages.return_entire_table()]):
            try:
                package_num = input("Input the package number you'd like to look up.\n")
            except:
                package_num = 0
        time = 25
        while 0 > time or time > 24:
            try:
                time = EricTime.reverse_time(input("Input a time, in 13:00 (24h) format, for the package.\n"))
            except:
                time = 25
                print("Incorrect format.")
        print()
        print(Package.packages.search(int(package_num)).get_package_status(time))
        print()
    elif user_input == "t":
        time = 25
        while 0 > time or time > 24:
            try:
                time = EricTime.reverse_time(input("Input a time, in 13:00 (24h) format, to get truck distance.\n"))
            except:
                time = 25
                print("Incorrect time or format.")
        total_distance = sum([(time - 8) * truck.speed for truck in all_trucks])
        # Get all waits
        all_waits = [[wait for wait in truck.wait_times] for truck in all_trucks]
        total_wait = 0
        # Add relevant waits to total
        for truck in all_waits:
            for wait in truck:
                try:
                    if time > wait[0] + wait[1]:
                        total_wait += wait[1]
                    elif time > wait[0]:
                        total_wait += time - wait[0]
                except IndexError:
                    continue
        # Deduct time if the truck's end of day time is less than the requested time
        for truck in all_trucks:
            if truck.time < time:
                total_distance -= (time - truck.time) * 18
        total_distance -= total_wait * 18
        print(f"Total distance for trucks at {EricTime.convert_time(time)}: {total_distance} miles.")
    user_input = input("Input t for truck info. Input p for package info. Input n to exit.\n")

