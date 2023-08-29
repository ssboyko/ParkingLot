# ParkingLot
Given a parking lot. The parking lot does not have limitations on the capacity of the
vehicles that can be parked. The parking lot is open from 06:00 in the morning until 23:30 at
night. As an input to your program, you get the log of all the entrances and exits of the vehicles
that have spent an amount of time in the parking lot.
Each log record will look as follows and contain the following data:
Vehicle Id – unique identifier of the vehicle.
Vehicle type – TRUCK/MOTORCYCLE/CAR.
Timestamp – timestamp in UTC time of the entrance log or exit log.
Action type – can be EXIT or ENTRANCE.
Example of logs =
[[“11353”, “TRUCK”, “2020-01-01T00:00:00.000Z”,“ENTRANCE” ],
[“11353”, “TRUCK”, “2020-01-01T02:00:00.000Z”,“EXIT” ],
[“86453”, “CAR”, “2020-01-01T04:24:00.000Z”,“ENTRANCE” ]]
