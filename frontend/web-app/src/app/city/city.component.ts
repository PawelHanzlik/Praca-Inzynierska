import { Component, OnInit } from '@angular/core';
import {AppService} from "../app-service";

@Component({
  selector: 'app-city',
  templateUrl: './city.component.html',
  styleUrls: ['./city.component.css']
})

export class CityComponent implements OnInit {

  cities: city[];
  selected: city;

  constructor(private appService: AppService) {

    this.selected = {
      id: 1,
      name: "Kraków"
    }
    this.cities = [{
      id: 1,
      name: "Kraków"
    },{
      id: 2,
      name: "Warszawa"
    },{
      id: 3,
      name: "Wrocław"
    }
    ]
  }

  ngOnInit(): void {
  }

  public generateData(): void {
    this.appService.generateData(this.selected.name);
  }
}
interface city{
  id: number;
  name: String;
}
