package com.flightinfo.app;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = FlightInfoApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface FlightInfoApplication_GeneratedInjector {
  void injectFlightInfoApplication(FlightInfoApplication flightInfoApplication);
}
