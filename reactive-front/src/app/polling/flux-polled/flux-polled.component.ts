import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { FluxPolledService } from '../flux-polled.service';

@Component({
  selector: 'app-flux-polled',
  templateUrl: './flux-polled.component.html',
  styleUrls: ['./flux-polled.component.css']
})
export class FluxPolledComponent implements OnInit {
  polledTransacaoStatus$: Observable<any>;
  constructor(private pollingService: FluxPolledService) { }

  ngOnInit(): void {
    this.polledTransacaoStatus$ = this.pollingService.getStatusInterval();
  }

}
