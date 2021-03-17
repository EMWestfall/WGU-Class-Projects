import math


class EricTime:
    @staticmethod
    def convert_time(float_time):
        if float_time is None or float_time == "":
            return
        float_time = float(float_time)
        hour = math.floor(float_time) % 24
        minute = round(60 * (float_time - math.floor(float_time)))
        if minute < 10:
            minute = "0" + str(minute)
        elif minute == 60:
            hour += 1
            minute = "00"
        return str(hour) + ":" + str(minute)

    @staticmethod
    def reverse_time(time):
        time = time.split(":")
        hour = time[0]
        minute = str(float(time[1])/60)[1:]
        return float(f"{hour}{minute}")

