import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AngularFirestore } from '@angular/fire/firestore';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  public transfers: Observable<any[]>;

  constructor(db: AngularFirestore) {
    //https://github.com/angular/angularfire/blob/master/docs/firestore/documents.md
    this.transfers = db.collection('/transfers').valueChanges();
  }
}

