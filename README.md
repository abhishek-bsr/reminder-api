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

## Environment Variables

- **[NOTE]** Create file `config.properties` and use the below variables  

| ENV Name   | Description          |
|------------|----------------------|
| DB_PORT    | Database port number |
| DB_NAME    | Database name        |
| PG_USER    | Postgres username    |
| PG_PASS    | Postgres password    |
| TABLE_NAME | Name of table        |

## Installation

```shell
# clone into repository
git clone https://github.com/abhishek-bsr/reminder-api.git

# get into directory
cd reminder-api
# create .env file in /resources
echo "#env data here" >> /src/main/resources/config.properties

# build using maven
mvn clean package

# copy target to /apache-tomcat-${version}/bin/webapps 
# inside /apache-tomcat-${version}/bin
# run command
./startup.sh

# to end, run command
./shutdown.sh
```
