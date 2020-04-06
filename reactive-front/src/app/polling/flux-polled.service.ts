import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { interval, timer } from 'rxjs';
import { switchMap, takeUntil, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FluxPolledService {

  constructor(private http: HttpClient) { }

    //Polling for a Mono
    getStatusInterval() {

      const status$ = this.http.get('http://localhost:8080/mono/status/1');
  
      return interval(5000)
        .pipe(
          switchMap((_: number) => status$),
          takeUntil(timer(60000)),
        )
        .pipe(map(data => data));
    }
}
