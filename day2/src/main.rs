use std::fs::File;
use std::io::{BufRead, BufReader};

fn is_valid_id(id: u64) -> bool {
    let str_id = id.to_string();
    let len = str_id.len();
    if len < 2 || len % 2 != 0 {
        return true;
    }

    let slices = str_id.split_at(len / 2);
    slices.0 != slices.1
}

fn main() -> std::io::Result<()> {
    let file = File::open("./day2-input.txt")?;
    let reader = BufReader::new(file);
    let line = reader.lines().next().unwrap()?;

    let ranges: Vec<&str> = line.split(',').collect();

    let mut invalid_count: u64 = 0;

    for range in ranges {
        let parts: Vec<&str> = range.split('-').collect();
        let lower: u64 = parts[0].parse().unwrap();
        let upper: u64 = parts[1].parse().unwrap();

        for id in lower..=upper {
            if !is_valid_id(id) {
                invalid_count += id;
            }
        }
    }

    println!("There are {} invalid ids.", invalid_count);

    Ok(())
}
