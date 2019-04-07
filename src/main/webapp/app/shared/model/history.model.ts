import { Moment } from 'moment';
import { IPrize } from 'app/shared/model//prize.model';

export interface IHistory {
    id?: number;
    date?: Moment;
    lotteryId?: number;
    prizes?: IPrize[];
}

export class History implements IHistory {
    constructor(public id?: number, public date?: Moment, public lotteryId?: number, public prizes?: IPrize[]) {}
}
