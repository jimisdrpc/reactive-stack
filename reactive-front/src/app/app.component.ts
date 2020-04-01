import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AngularFirestore } from '@angular/fire/firestore';

@Component({
  selector: 'app-root',
  template: `
      <ul>
          <li *ngFor="let transfer of transfers | async">
              <pre>{{ transfer | json }}</pre>
          </li>
      </ul>
  `
})
export class AppComponent {
  public transfers: Observable<any[]>;

  constructor(db: AngularFirestore) {
    //https://github.com/angular/angularfire/blob/master/docs/firestore/documents.md
    this.transfers = db.collection('/transfers').valueChanges();
  }
}

