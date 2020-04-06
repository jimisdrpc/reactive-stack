import { Injectable, NgZone } from '@angular/core';
import { Observable, interval, timer } from 'rxjs';
import { Status } from './../model/status';
import { HttpClient } from '@angular/common/http';
import { switchMap, takeUntil, map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class SseFluxStatusService {

  status: Status[] = [];
  constructor(private _zone: NgZone, private http: HttpClient) { }

  //SSE
  getServerSentEvent(url: string): Observable<any> {
    this.status = [];
    return Observable.create(observer => {
      const eventSource = this.getEventSource(url);
      eventSource.onmessage = event => {
        this._zone.run(() => {
          let json = JSON.parse(event.data);
          this.status.push(new Status(json['id'], json['status']));
          observer.next(this.status);
        });
      };
      eventSource.onerror = (error) => {
        if (eventSource.readyState === 0) {
          console.log('The stream has been closed by the server.');
          eventSource.close();
          observer.complete();
        } else {
          observer.error('EventSource error: ' + error);
        }
      }

    });
  }
  private getEventSource(url: string): EventSource {
    return new EventSource(url);
  }
}





