export enum ConvenienceType {
  PRIVATE_BATHROOM,
  BALCONY,
  KITCHEN,
  AIR_CONDITIONING,
  POOL,
  TV,
  BREAKFAST,
  WASHING_MACHINE,
  DINNER,
  SAUNA,
  PARKING
}

export function getConvenienceTypeText(convenienceType: unknown): string {
  switch (convenienceType) {
    case 'PRIVATE_BATHROOM':
      return 'Private Bathroom';
    case 'BALCONY':
      return 'Balcony';
    case 'KITCHEN':
      return 'Kitchen';
    case 'AIR_CONDITIONING':
      return 'Air Conditioning';
    case 'POOL':
      return 'Pool';
    case 'TV':
      return 'TV';
    case 'BREAKFAST':
      return 'Breakfast';
    case 'WASHING_MACHINE':
      return 'Washing Machine';
    case 'DINNER':
      return 'Dinner';
    case 'SAUNA':
      return 'Sauna';
    case 'PARKING':
      return 'Parking';
    default:
      return 'Unknown';
  }
}
