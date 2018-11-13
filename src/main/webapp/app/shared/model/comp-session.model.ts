import { IScore } from 'app/shared/model//score.model';

export interface ICompSession {
  id?: number;
  sessionName?: string;
  level?: string;
  sessionScores?: IScore[];
}

export const defaultValue: Readonly<ICompSession> = {};
