package com.flightinfo.app.service;

import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = FlightNotificationService.class
)
@GeneratedEntryPoint
@InstallIn(ServiceComponent.class)
public interface FlightNotificationService_GeneratedInjector {
  void injectFlightNotificationService(FlightNotificationService flightNotificationService);
}
