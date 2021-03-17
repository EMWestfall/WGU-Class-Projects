from enum import Enum
from HashTable import HashTable


class DeliveryStatus(Enum):
    AT_HUB = "at the hub"
    EN_ROUTE = "en route"
    DELIVERED = "delivered"
    EN_ROUTE_TO_HUB = "en route to hub"


class LocationType(Enum):
    LOCATION = 0
    TRUCK = 1


class Package:

    # Statics
    packages = HashTable()
    bundles = {}

    @staticmethod
    def get_all_packages_as_str():
        return [str(package) + "\n" for package in Package.packages.return_entire_table()]

    @staticmethod
    def get_all_packages():
        return Package.packages.return_entire_table()

    # Overrides
    def __init__(self, number, weight, deadline, location_to_deliver_to, hub_arrival=8.00,
                 current_location=(0, LocationType.LOCATION), delivery_status=DeliveryStatus.AT_HUB,
                 truck_requirement=None, bundle=None):
        from math import isnan
        self.number = number
        self.weight = weight
        self.deadline = deadline
        self.location_to_deliver_to = location_to_deliver_to
        self.hub_arrival = hub_arrival
        self.current_location = current_location
        self.delivery_status = delivery_status
        self.delivery_time = None
        self.loading_time = None
        self.delivery_truck = None
        if isnan(truck_requirement):
            self.truck_requirement = None
        else:
            self.truck_requirement = int(truck_requirement)
        try:
            if isnan(bundle):
                self.bundle = None
            else:
                self.bundle = int(bundle)
        except TypeError:
            self.bundle = None
        if bundle is not None:
            try:
                Package.bundles[self.bundle].append(self)
            except KeyError:
                Package.bundles[self.bundle] = [self]

        Package.packages.insert(self, self.number)

    def __str__(self):
        from Location import Location
        from EricTime import EricTime
        curr_loc = None
        if self.current_location[1] is LocationType.LOCATION:
            curr_loc = Location.locations.search(self.current_location[0]).address
        elif self.current_location[1] is LocationType.TRUCK:
            curr_loc = f"Truck {self.current_location[0]}"
        del_loc = Location.locations.search(self.location_to_deliver_to).address
        return f"\nPackage Number: {self.number}\n" \
               f"Weight: {self.weight} kg\n" \
               f"Status: {self.delivery_status}\n" \
               f"Deadline: {EricTime.convert_time(self.deadline)}\n" \
               f"Delivered At: {EricTime.convert_time(self.delivery_time)}\n" \
               f"Current Location:\n{curr_loc}\n\n" \
               f"Delivery Location:\n{del_loc}"

    # This method sets a new location for the package and also updates the package holder's package_dict
    def set_location(self, number, location_type=LocationType.LOCATION):
        from Location import Location
        from Truck import Truck
        # remove this package from the holder it is jumping from
        if self.current_location[1] is LocationType.LOCATION:
            Location.locations.search(self.current_location[0]).packages.pop(self.number)
        elif self.current_location[1] is LocationType.TRUCK:
            Truck.trucks.search(self.current_location[0]).packages.pop(self.number)

        # set the new location
        self.current_location = (number, location_type)

        # add this package to the new location
        if self.current_location[1] is LocationType.LOCATION:
            Location.locations.search(self.current_location[0]).packages[self.number] = self
        elif self.current_location[1] is LocationType.TRUCK:
            Truck.trucks.search(self.current_location[0]).packages[self.number] = self

    # This method loads the package onto a truck
    def load(self, truck_num, time):
        from Truck import Truck
        from Location import Location
        Truck.trucks.search(truck_num).destination_set.add(Location.locations.search(self.location_to_deliver_to))
        self.set_location(truck_num, LocationType.TRUCK)
        self.delivery_status = DeliveryStatus.EN_ROUTE
        self.loading_time = time
        self.delivery_truck = truck_num

    # This method delivers the package, recording the time
    def deliver(self, location_num, time):
        self.set_location(location_num)
        self.delivery_status = DeliveryStatus.DELIVERED
        self.delivery_time = time

    # Report
    def get_package_status(self, time):
        from Location import Location
        from EricTime import EricTime
        if self.hub_arrival > time:
            status = "En route to hub"
        elif self.loading_time > time:
            status = "At hub"
        elif self.delivery_time > time:
            status = "En route"
        else:
            status = f"Delivered at {EricTime.convert_time(self.delivery_time)}"
        return f"ID: {self.number}\n" \
               f"Delivery Address: {Location.locations.search(self.location_to_deliver_to).address}\n" \
               f"Delivery City: {Location.locations.search(self.location_to_deliver_to).city}\n" \
               f"Delivery Zip: {Location.locations.search(self.location_to_deliver_to).zip_code}\n" \
               f"Weight: {self.weight}\n" \
               f"Status: {status}"
