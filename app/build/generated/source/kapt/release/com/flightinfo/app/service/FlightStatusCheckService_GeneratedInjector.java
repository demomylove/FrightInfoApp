package com.flightinfo.app.service;

import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = FlightStatusCheckService.class
)
@GeneratedEntryPoint
@InstallIn(ServiceComponent.class)
public interface FlightStatusCheckService_GeneratedInjector {
  void injectFlightStatusCheckService(FlightStatusCheckService flightStatusCheckService);
}
