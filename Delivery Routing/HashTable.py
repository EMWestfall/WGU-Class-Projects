from enum import Enum
from Trees import AVL


class HashType(Enum):
    CHAIN = 0
    LINEAR = 1
    QUADRATIC = 2


class EmptyType(Enum):
    EMPTY_SINCE_START = -1
    EMPTY_AFTER_REMOVAL = -2


class HashTable:
    def __init__(self, objs=[], hash_type=HashType.QUADRATIC, size=10, c1=1, c2=1):
        # if the hash table uses quadratic probing, then round the size to the nearest power of 2
        if hash_type is HashType.QUADRATIC:
            from math import ceil
            from math import log
            self.size = pow(2, ceil(log(size) / log(2)))
        else:
            self.size = size
        self.item_count = 0
        # Initialize the table
        self.table = []
        # Check that all values are positive
        for obj in objs:
            if HashTable.to_value(obj) < 0:
                print("HashTable only takes positive values.")
                return
        # Chain hash tables have a list at each index
        if hash_type is HashType.CHAIN:
            for i in range(self.size):
                self.table.append([])
        # open addressing hash tables need empty_since_start initialization
        else:
            # Index is a list of index numbers that contain items; it's only needed for open addressed hash tables
            self.index = AVL()
            for i in range(self.size):
                self.table.append(EmptyType.EMPTY_SINCE_START)
        # Initialize other values
        self.hash_type = hash_type
        self.c1 = c1
        self.c2 = c2
        if objs:
            for obj in objs:
                if len(str(obj)) > 0:
                    self.insert(obj)

    @staticmethod
    def to_value(obj):
        if isinstance(obj, int):
            return obj
        string = str(obj)
        total = 0
        for ch in string:
            if ch.isdigit():
                total += int(ch)
            elif ch.isalpha():
                total += ord(ch)
        return total

    # Calculates the hash location based on the value and mode
    # Insert mode finds the first available bucket
    # Search mode keeps searching until either an empty_from_start bucket or a number of searches == table size is
    # reached.
    def calculate_hash(self, value, mode="insert"):
        if self.hash_type == HashType.CHAIN:
            return value % self.size
        if mode == "insert":
            if self.hash_type == HashType.LINEAR:
                bucket = value % self.size
                # Keep searching until a free bucket is found, then insert the value and break the loop
                while True:
                    if self.table[bucket] is EmptyType.EMPTY_SINCE_START or \
                            self.table[bucket] is EmptyType.EMPTY_AFTER_REMOVAL:
                        return bucket
                    else:
                        bucket = (bucket + 1) % self.size
            elif self.hash_type == HashType.QUADRATIC:
                i = 0
                bucket = (value + self.c1 * i + self.c2 * i**2) % self.size
                # Keep searching until a free bucket is found, then insert the value and break the loop
                while True:
                    if self.table[bucket] is EmptyType.EMPTY_SINCE_START or \
                            self.table[bucket] is EmptyType.EMPTY_AFTER_REMOVAL:
                        return bucket
                    else:
                        i += 1
                        bucket = (value + self.c1 * i + self.c2 * i ** 2) % self.size
        elif mode == "search":
            count = 0
            if self.hash_type == HashType.LINEAR:
                bucket = value % self.size
                while self.table[bucket] is EmptyType.EMPTY_AFTER_REMOVAL and count < self.size:
                    bucket = (bucket + 1) % self.size
                    count += 1
                # if the item is not found in the search, then return None as a failure
                if count >= self.size:
                    return None
                return bucket
            elif self.hash_type == HashType.QUADRATIC:
                i = 0
                bucket = (value + self.c1 * i + self.c2 * i ** 2) % self.size
                while self.table[bucket] is EmptyType.EMPTY_AFTER_REMOVAL and count < self.size:
                    i += 1
                    bucket = (value + self.c1 * i + self.c2 * i ** 2) % self.size
                    count += 1
                # if the item is not found in the search, then return None as a failure
                if count >= self.size or self.table[bucket] is EmptyType.EMPTY_SINCE_START:
                    return None
                return bucket

    def insert(self, obj, value=None):
        # Validations/Setups
        # Quadratic HashTables can only guarantee a spot if the table is half full or less
        if self.hash_type is HashType.QUADRATIC and self.item_count * 2 >= self.size:
            self.expand()
        # Chain hash tables don't need to expand, they deal with collisions in an alternate way
        # Other hashtable types should not get too full in order to stay efficient
        elif (self.hash_type is not HashType.CHAIN) and self.item_count * 1.5 >= self.size:
            self.expand()

        if value is None:
            # Don't insert if negative value
            if self.to_value(obj) < 0 or value < 0:
                print("Hash table only accepts positive values.")
                return
            value = self.calculate_hash(self.to_value(obj))

        # Calculate the hash to insert to
        value = self.calculate_hash(value)
        if self.hash_type is HashType.CHAIN:
            self.table[value].append(obj)
        else:
            self.table[value] = obj
            self.index.add(value)
        self.item_count += 1

    # This method expands the HashTable and recalculates the old values
    def expand(self, multiplication_factor=2):
        # Copy the table to another instance
        old_table = self.table.copy()
        # Get the index before it's cleared if this is an open addressed hash table
        traversal = []
        if self.hash_type is not HashType.CHAIN:
            self.index.inorder()
            nodes = self.index.ordered_nodes
            for node in nodes:
                if node is not None:
                    traversal.append(node.value)
        # Reset the table and index
        self.table.clear()
        self.index.clear()
        # Item count will increment when items are re-added
        self.item_count = 0
        # Expand the size
        self.size *= multiplication_factor
        # Initialization is different for CHAIN versus others
        if self.hash_type is HashType.CHAIN:
            # Expand the size
            for i in range(self.size):
                self.table.append([])
            # Set new size
            self.size = len(self.table)
            # Recalculate old values
            for old_bucket in old_table:
                for item in old_bucket:
                    self.insert(item)
        else:
            for i in range(self.size):
                self.table.append(EmptyType.EMPTY_SINCE_START)
            # Recalculate old values
            for num in traversal:
                try:
                    self.insert(old_table[num], old_table[num].number)
                except:
                    self.insert(old_table[num], num)

    def remove(self, obj):
        if self.to_value(obj) < 0:
            print("Hash table values cannot be negative.")
            return
        value = HashTable.to_value(obj)
        value = self.calculate_hash(value, "search")
        if self.hash_type == HashType.CHAIN:
            for i in range(len(self.table[value])):
                if self.table[value][i] == obj:
                    self.table[value].pop(i)
                    self.item_count -= 1
        elif value is not None and self.index.search(value) is not None:
            self.table[value] = EmptyType.EMPTY_AFTER_REMOVAL
            self.index.remove(self.index.search(value))
            self.item_count -= 1
        else:
            print(f"Object not found, cannot remove.")

    def search(self, value):
        value = self.calculate_hash(value, "search")
        if value is None:
            return None
        return self.table[self.calculate_hash(value, "search")]

    def is_in_table(self, obj):
        value = HashTable.to_value(obj)
        value = self.calculate_hash(value, "search")
        return value > -1

    def return_entire_table(self):
        return_list = []
        if self.hash_type == HashType.CHAIN:
            for bucket in self.table:
                for item in bucket:
                    return_list.append(item)
        else:
            self.index.inorder()
            for node in self.index.ordered_nodes:
                if node is not None:
                    return_list.append(self.table[node.value])
        return return_list

    def get_index(self):
        if self.hash_type != HashType.CHAIN:
            values = []
            self.index.inorder()
            for node in self.index.ordered_nodes:
                if node is not None:
                    values.append(node.value)
            return values
        else:
            print("Chained hash tables don't have indexes.")
            return
