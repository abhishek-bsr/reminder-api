# Reminder API

## APIs

### GET
```shell
# get all data from query
HTTP GET /api/v1/reminders

# get data from query
# limit<int>   = restrict number of rows from query
# offset<int>  = skip number of rows from query
HTTP GET /api/v1/reminders?limit=<value>&offset=<value>

# get specific data from query
HTTP GET /api/v1/reminders/${id}
```

### DELETE
```shell
# delete all data from query
HTTP DELETE /api/v1/reminders

# delete specific data from query
HTTP DELETE /api/v1/reminders/${id}
```

### POST
```shell
# post data with fields into database
# sample body:
# {
#   "reminders": { 
#     "name": <String | Required>,
#     "reminder_utc": <String | Required>,
#     "is_completed": <Boolean | Optional>,
#     "is_important": <Boolean | Optional>,
#     "note": <String | Optional>
#   }
# }
HTTP POST /api/v1/reminders
```

### PUT
```shell
# update data with fields into database
# sample body:
# {
#   "reminders": { 
#     "name": <String | Optional>,
#     "reminder_utc": <String | Optional>,
#     "is_completed": <Boolean | Optional>,
#     "is_important": <Boolean | Optional>,
#     "note": <String | Optional>
#   }
# }
HTTP PUT /api/v1/reminders/${id}
```

## Installation

```shell
# clone into repository
git clone https://github.com/abhishek-bsr/reminder-api.git

# build using maven
mvn clean package

# copy target to /apache-tomcat-${version}/bin/webapps 
# inside /apache-tomcat-${version}/bin
# run command
./startup.sh

# to end, run command
./shutdown.sh
```
