
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';

//Configs
import { environment } from '../environments/environment';

//angularfire imports
import { AngularFireModule } from "@angular/fire";
import { AngularFirestoreModule } from "@angular/fire/firestore";

//Project Components
import { AppComponent } from './app.component';
import { SseFluxStatusComponent } from './sse/sse-flux-status/sse-flux-status.component';
import { HttpClientModule } from '@angular/common/http';
import { FluxPolledComponent } from './polling/flux-polled/flux-polled.component';
import { FluxPolledService } from './polling/flux-polled.service';

@NgModule({
  declarations: [
    AppComponent,
    SseFluxStatusComponent,
    FluxPolledComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFirestoreModule
  ],
  providers: [FluxPolledService],
  bootstrap: [AppComponent]
})
export class AppModule { }
