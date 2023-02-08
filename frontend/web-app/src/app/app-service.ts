import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import { Observable } from 'rxjs';

export interface User {
  userId: number;
  preferableZone: number;
  name: string;
  surname: string;
  age: number;
}
@Injectable({
  providedIn: 'root'
})
export class AppService {

  private apiServerUrl = environment.apiBaseUrl;
  requestOptions: Object = {
    responseType: 'text'
  }
  response = ""
  constructor(private http: HttpClient) { }

  public generateData(): void {
     this.http.get<String>(`${this.apiServerUrl}/api/maxsat/GenerateData`).subscribe(
       response => { console.log(response)}
     );
  }

  public assignCity(city: String): void {
    this.http.get<String>(`${this.apiServerUrl}/api/maxsat/AssignCity?city=${city}`).subscribe(
      response => { console.log(response)}
    );
  }

  public solve(cordX: Number, cordY: Number,usersChoices: String []): void {
    this.http.get<String>(`${this.apiServerUrl}/api/maxsat/sfps?CordX=${cordX}&CordY=${cordY}&usersChoices=${usersChoices}`, this.requestOptions).subscribe(
      response => { console.log(response) ; this.response = String(response)}
    );
  }

  public createUser(value: any): void {
    this.http.get<String>(`${this.apiServerUrl}/api/maxsat/createUser?name=${value.name}&surname=${value.surname}&age=${value.age}`, this.requestOptions).subscribe(
      response => { console.log(response) ; this.response = String(response)}
    );
  }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiServerUrl}/api/maxsat/users`);
  }

  public saveTrip(userId: BigInt, time: string, cordX: Number, cordY: Number,usersChoices: String []): void{
    this.http.post(`${this.apiServerUrl}/api/maxsat/saveTrip?userId=${userId}&time=${time}&x=${cordX}&y=${cordY}&usersChoices=${usersChoices}`, {}).subscribe(
      response => {}
    )
  }

  public getReccomendations<String>(userId: BigInt, time: string) : Observable<any>{
    return this.http.get<String>(`${this.apiServerUrl}/api/maxsat/recommend?userId=${userId}&time=${time}`, this.requestOptions)
  }}
