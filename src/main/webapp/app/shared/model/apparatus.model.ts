import { IScore } from 'app/shared/model//score.model';

export interface IApparatus {
  id?: number;
  apparatusName?: string;
  competitionType?: string;
  apparatusScores?: IScore[];
}

export const defaultValue: Readonly<IApparatus> = {};
