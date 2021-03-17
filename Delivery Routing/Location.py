from PackageHolder import PackageHolder
from HashTable import HashTable


class Location(PackageHolder):
    # statics
    locations = HashTable()

    @staticmethod
    def all_locations_str():
        output = "Locations\n\n"
        for value in Location.locations.return_entire_table():
            output += f"{str(value)}\n"
        return output

    @staticmethod
    def get_all_locations():
        return Location.locations.return_entire_table()

    @staticmethod
    def max_location_number():
        Location.locations.index.inorder()
        Location.locations.index.ordered_values.clear()
        ordered_values = []
        for node in Location.locations.index.ordered_nodes:
            ordered_values.append(node.value)
        return max(ordered_values)

    # Overrides
    def __init__(self, number, address, city="", state="", zip_code=99999, distance_list=None):
        super().__init__()
        self.number = number
        self.address = address
        self.city = city
        self.state = state
        self.zip_code = zip_code
        self.distance_list = distance_list
        self.packages_going_here = []
        self.cluster = 0
        Location.locations.insert(self, number)

    def __str__(self):
        output = f"Location ID: {self.number}\nAddress: {self.address}\nCity: {self.city}\n" \
                 f"State: {self.state}\nZip: {self.zip_code}\n{super().__str__()}"
        return output

    def get_path_to(self, to_location):
        return self.distance_list[to_location][0]

    def get_distance_to(self, to_location):
        return self.distance_list[to_location][1]
