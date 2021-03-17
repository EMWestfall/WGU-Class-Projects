class PackageHolder:
    class LocationType:
        LOCATION = 0
        TRUCK = 1

    # Overrides
    def __init__(self):
        self.packages = {}  # syntax is {id : Package}

    def __str__(self):
        package_list = ""
        for pkg in self.packages.values():
            package_list += f"{str(pkg)}\n"
        return f"Packages at Location: {package_list}"

    def get_package_nums(self):
        package_list = []
        [package_list.append(package.number) for package in self.packages.values()]
        return package_list
