import { IPrize } from 'app/shared/model//prize.model';

export interface ICategory {
    id?: number;
    title?: string;
    prizes?: IPrize[];
}

export class Category implements ICategory {
    constructor(public id?: number, public title?: string, public prizes?: IPrize[]) {}
}
