export interface IPrize {
    id?: number;
    title?: string;
    value?: number;
    categoryId?: number;
    lotteryId?: number;
    historyId?: number;
}

export class Prize implements IPrize {
    constructor(
        public id?: number,
        public title?: string,
        public value?: number,
        public categoryId?: number,
        public lotteryId?: number,
        public historyId?: number
    ) {}
}
