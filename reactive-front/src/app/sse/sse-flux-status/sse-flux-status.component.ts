//ANGULAR
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

//SERVICES
import { SseFluxStatusService } from './../sse-flux-status.service';

//MODELS
import { Status } from './../../model/status';

//RXJS
import { Observable, timer, interval, of } from 'rxjs';
import { concatMap, map, tap, switchMap, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-sse-flux-status',
  templateUrl: './sse-flux-status.component.html',
  styleUrls: ['./sse-flux-status.component.css']
})
export class SseFluxStatusComponent implements OnInit {

  status$: Observable<any>;
  constructor(private sseFluxStatusService: SseFluxStatusService) { }

  ngOnInit(): void {
    this.status$ = this.sseFluxStatusService
      .getServerSentEvent("http://localhost:8080/stream/transfers");
  }

}









