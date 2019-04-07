import { IPrize } from 'app/shared/model//prize.model';
import { IHistory } from 'app/shared/model//history.model';

export interface ILottery {
    id?: number;
    title?: string;
    minparticipants?: number;
    maxparticipnts?: number;
    price?: number;
    userProfileId?: number;
    prizes?: IPrize[];
    histories?: IHistory[];
}

export class Lottery implements ILottery {
    constructor(
        public id?: number,
        public title?: string,
        public minparticipants?: number,
        public maxparticipnts?: number,
        public price?: number,
        public userProfileId?: number,
        public prizes?: IPrize[],
        public histories?: IHistory[]
    ) {}
}
