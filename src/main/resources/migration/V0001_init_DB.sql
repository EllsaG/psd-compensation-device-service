create table compensation_device
(
    compensation_device_id int2 primary key,
    model_of_compensation_device varchar(255) not null,
    reactive_power_of_compensation_device float4 not null
);

create table compensation_device_selection
(
    compensation_device_selection_id  int2 primary key,
    min_power_of_compensation_device float4 not null,
    max_power_of_compensation_device float4 not null
);